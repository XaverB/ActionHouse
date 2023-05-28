import { Component } from '@angular/core';
import {NgForm} from "@angular/forms";
import {UserService} from "../user.service";
import {AuthService, LoginDto} from "../api-client";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  user: LoginDto = {
    email: '',
    password: ''
  };

  constructor(private userService: AuthService,
              private router: Router) {}

  onSubmit(form: NgForm) {
    if (form.valid) {
      this.userService.login(this.user).subscribe(
        response => {

          if(response instanceof HttpErrorResponse){
            console.error(`Login error`, response.error);
          }
          else {
            console.log("Login success");
            localStorage.setItem('token', <string> this.user.email);
            this.router.navigate(['/articles']);
          }
        }
      );
    }
  }
}
