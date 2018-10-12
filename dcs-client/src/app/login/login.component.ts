import { Component, OnInit, Input } from '@angular/core';
import { ChatService } from '../chat.service';
import { LoginRequest } from '../requests';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  request: LoginRequest = new LoginRequest();

  constructor(private service: ChatService, private router: Router) { }

  login() {
    this.service.login(this.request);
    // .subscribe();
  }

}
