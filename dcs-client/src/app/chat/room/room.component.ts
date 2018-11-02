import { Component, OnInit, Inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Route } from '@angular/compiler/src/core';
import {ChatService} from "../../service/chat.service";

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.scss']
})
export class RoomComponent implements OnInit {

  name;

  constructor(private route: ActivatedRoute, private router: Router, private chat: ChatService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.name = params['name'];
    });
  }

  leaveRoom(){
    this.chat.leaveRoom(this.name)
      .subscribe(() => {
        this.router.navigate(['/']);
      }, err => console.error(err));
  }

}
