import { Component, OnInit, Inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ChatService } from 'src/app/service/chat.service';
import {filter, throttle} from 'rxjs/operators';

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.scss']
})
export class RoomComponent implements OnInit {

  name: string;
  message: string = '';
  writingUsers = [];

  constructor(
    private chat: ChatService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.name = params['name'];
    });

    this.chat
      .onRoomDeleted()
      .pipe(filter(name => this.name === name))
      .subscribe(name => this.router.navigateByUrl('/'));
  }

  ngAfterViewInit(){
    const input:HTMLInputElement = document.querySelector('#message');
    this.chat.registerRoomInputListener(input, this.name);
    this.chat.registerUsersWritingListener(this.name, this.writingUsers)
  }

  sendMessage() {
    console.log("Room name: " + this.name + " Message content: " + this.message);
    this.chat.sendMessage(this.name, this.message)
    .subscribe (
      _ => this.message = '',
      err => console.error(err)
    );
  }

}
