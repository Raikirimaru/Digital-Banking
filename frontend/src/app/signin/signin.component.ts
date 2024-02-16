import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent {
  signinForm! : FormGroup

  constructor(private fbuild : FormBuilder, private authService : AuthService, private router : Router) {}

  ngOnInit() {
    this.signinForm = this.fbuild.group({
      username: this.fbuild.control(''),
      password: this.fbuild.control('')
    })
  }

  handleSignIn() {
    let username = this.signinForm.value.username
    let password = this.signinForm.value.password
    this.authService.signin(username, password).subscribe({
      next: (res) => {
        console.log(res)
        this.authService.loadProfile(res)
        this.router.navigateByUrl("/admin/home")
        Swal.fire({
          title: "Good job!",
          text: "Authentication successful",
          icon: "success"
        });
      },
      error: (err) => {
        Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "Something went wrong!",
        });
        console.log(err)
      }
    })
  }
}
