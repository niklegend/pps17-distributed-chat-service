import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { EventBusService } from './event-bus.service';
import { Room, Participation } from '../model';
import { CreateRoomRequest, DeleteRoomRequest } from '../requests';
import { AuthService } from './auth.service';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private static API_PREFIX = '/api';

  private static EVENTS = ChatService.API_PREFIX + '/events';

  private static ROOMS = ChatService.API_PREFIX + '/rooms';

  private static ROOM_CREATED = 'rooms.created';
  private static ROOM_DELETED = 'rooms.deleted';

  private roomCreated = new Subject<Room>();
  private roomDeleted = new Subject<string>();
  private roomSelected = new Subject<Room>();

  constructor(
    private http: HttpClient,
    private eventBus: EventBusService,
    private auth: AuthService
  ) {
    eventBus.connect(ChatService.EVENTS);
    eventBus.registerHandler(ChatService.ROOM_DELETED, (err, msg) => {
      this.roomDeleted.next(msg.body.name);
    });
  }

  selectRoom(room: Room) {
    this.roomSelected.next(room);
  }

  onRoomSelected(): Observable<Room> {
    return this.roomSelected.asObservable();
  }

  createRoom(name: string): Observable<Participation> {
    const user = this.auth.user;
    return this.http
      .post<Participation>(
        '/api/rooms',
        new CreateRoomRequest(name, user.username, user.token)
      )
      .pipe(tap(p => this.selectRoom(p.room)));
  }

  deleteRoom(name: string): Observable<Room> {
    const user = this.auth.user;
    const requestBody = new DeleteRoomRequest(name, user.username, user.token);
    return this.http.request<Room>('delete', ChatService.ROOMS, {
      body: requestBody
    });
  }

  onRoomCreated(): Observable<Room> {
    return this.roomCreated.asObservable();
  }

  onRoomDeleted(): Observable<string> {
    return this.roomDeleted.asObservable();
  }

}
