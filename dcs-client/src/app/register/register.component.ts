import { Component, OnInit, Input } from '@angular/core';
import { ChatService } from '../chat.service';
import { Router } from '@angular/router';
import { RegisterRequest } from '../requests';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  request = new RegisterRequest();

  constructor(private service: ChatService, private router: Router) { }

  register() {
    this.service.register(this.request);
    // .subscribe();
  }

}
