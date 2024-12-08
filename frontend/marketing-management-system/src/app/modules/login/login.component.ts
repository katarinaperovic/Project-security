import { Component, SecurityContext, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/services/auth/auth.service';
import { UserService } from 'src/app/services/user/user.service';
import { ReCaptcha2Component } from 'ngx-captcha';

import { NgForm } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  @ViewChild('recaptcha') recaptcha!: ReCaptcha2Component;
  user: User = {
    name: '',
    surname: '',
    firmName:'',
    pib:'',
    phone:'',
    confirmpassword: '',
    email: '',
    password: '',
    address: null,
    packageType:null
  };

  token: string|undefined;

  constructor(
    private userService: UserService,
    private toast: ToastrService,
    private router: Router,
    private authService: AuthService,
    private sanitizer: DomSanitizer
  ) {this.token = undefined;}

  onCaptchaResolved(token: string) {
    this.token = token;
    console.log(this.token);
  }
  login(form: NgForm) {
    if (this.user.email !== null) { 
      this.user.email = this.sanitizer.sanitize(SecurityContext.HTML, this.user.email) ?? '';
  }
  
  if (this.user.password !== null) { 
    this.user.password = this.sanitizer.sanitize(SecurityContext.HTML, this.user.password) ?? '';
  }


if(this.validateForm()){
    if (form.invalid) {
      for (const control of Object.keys(form.controls)) {
        form.controls[control].markAsTouched();
      }
      return;
    }
    if (!this.token) {
      this.toast.error('Please complete the reCAPTCHA verification');
      return;
    }
    this.userService.loginUser(this.user,this.token).subscribe((res) => {
        this.authService.setToken(res.accessToken, res.refreshToken);
        this.toast.success('', 'Logged in');
        this.router.navigate(['/']);
        
      },
      (error) => {
        
        if (error.status === 401) {
          this.toast.error('Invalid!');
           
        } else {
          this.toast.error('An error occurred');
          
        }
        this.resetForm(form);
      }
      );}
  }



  validateForm(): boolean {
  if(
    !this.user.email ||
    !this.user.password 
  ) {
    this.toast.error('You must fill all fields');
    return false;
  }

  const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (!emailPattern.test(this.user.email)) {
    this.toast.error('You must enter a valid email address');
    return false;
  }

  const passwordPattern = /^(?=.*[A-Z])(?=.*[@$!%*?&])(?=.*[a-z]).{8,}$/;
  if (!passwordPattern.test(this.user.password)) {
    this.toast.error(
      'Password must contain lowercase and uppercase letter, special character, and be minimum 8 characters long'
    );
    return false;
  }


  return true;
}

private resetCaptcha() {
  this.recaptcha.resetCaptcha();
}
private resetForm(form: NgForm) {
  form.resetForm();
  this.resetCaptcha();
}
}