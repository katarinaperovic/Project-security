import { Component, SecurityContext } from '@angular/core';
import { UserService } from 'src/app/services/user/user.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { PackageType, User } from 'src/app/model/user';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  user: User = {
    name: null,
    surname: null,
    firmName: null, 
    pib: null,
    phone:'',
    email: '',
    password: '',
    confirmpassword: '',
    address: null,
    packageType: null,
  };

  public street: string = '';
  public city: string = '';
  public state: string = '';
  public confirmPassword: any = '';
  public registrationType: string = 'individual';

  constructor(
    private userService: UserService,
    private toast: ToastrService,
    private router: Router,
    private sanitizer: DomSanitizer
  ) {}

  register() {
    if (this.validateForm()) {
      var address = { 
        city: this.sanitizeInput(this.city),
        state: this.sanitizeInput(this.state),
        street: this.sanitizeInput(this.street),
      }
      this.user.address = address; 
      this.user.confirmpassword = this.sanitizeInput(this.confirmPassword);
      const sanitizedUser = this.sanitizeUser(this.user);
      this.userService.register(sanitizedUser).subscribe(
        (res) => {
          this.toast.success('', 'Registration successfull');
          this.router.navigate(['/login']);
        },
        (error) => {
          if (error.status === 409) {
            this.toast.error('Email exists!')    
          } else {
            this.toast.error('Wrong data');
          }
        }
      );
    }
  }


  sanitizeInput(input: string): string {
    
// pomocu biblioteka al ukloni onda komplet 
    
  const safeText = this.sanitizer.sanitize(SecurityContext.HTML, input);

  return safeText ||'';
/*
  return input
  .replace(/</g, '&lt;')
  .replace(/>/g, '&gt;')
  .replace(/&/g, '&amp;')
  .replace(/"/g, '&quot;')
  .replace(/'/g, '&#39;');*/

  }

  sanitizeUser(user: User): User {
    if (this.registrationType === 'company') {
      console.log(user.firmName);
      console.log(this.sanitizeInput(user.firmName??''));

        return {
            ...user,
            name: null,
            surname: null,
            firmName: this.sanitizeInput(user.firmName ?? ""),
           //firmName:user.firmName,
            pib: this.sanitizeInput(user.pib ?? ""),
            phone: this.sanitizeInput(user.phone),
            email: this.sanitizeInput(user.email),
            password: this.sanitizeInput(user.password),
            confirmpassword: this.sanitizeInput(user.confirmpassword),
            address: {
                city: this.sanitizeInput(user.address?.city ?? ''),
                state: this.sanitizeInput(user.address?.state ?? ''),
                street: this.sanitizeInput(user.address?.street ?? '')
            },
            packageType: user.packageType
        };
    } else if (this.registrationType === 'individual') {
        return {
            ...user,
            name: this.sanitizeInput(user.name ?? ''),
            surname: this.sanitizeInput(user.surname ?? ''),
            firmName: null,
            pib: null,
            phone: this.sanitizeInput(user.phone),
            email: this.sanitizeInput(user.email),
            password: this.sanitizeInput(user.password),
            confirmpassword: this.sanitizeInput(user.confirmpassword),
            address: {
                city: this.sanitizeInput(user.address?.city ?? ''),
                state: this.sanitizeInput(user.address?.state ?? ''),
                street: this.sanitizeInput(user.address?.street ?? '')
            },
            packageType: user.packageType
        };
    } else {
        return user; 
    }
}


  validateForm(): boolean {
      if (
        (this.registrationType === 'individual' && (!this.user.name || !this.user.surname)) ||
        (this.registrationType === 'company' && (!this.user.firmName || !this.user.pib)) ||    
      !this.street ||
      !this.city ||
      !this.state ||
      !this.user.email ||
      !this.user.password ||
      !this.confirmPassword||
      !this.user.packageType||
      !this.user.phone
    ) {
      this.toast.error('You must fill all fields');
      return false;
    }

    const namePattern = /^[a-zA-Z\s]+$/;
    if (this.registrationType === 'individual' && this.user.name!=null &&!namePattern.test(this.user.name)) {
      this.toast.error('Name must contains only letters');
      return false;
    }

    const surnamePattern = /^[a-zA-Z\s]+$/;
    if (this.registrationType === 'individual' && this.user.surname!=null &&!surnamePattern.test(this.user.surname)) {
      this.toast.error('Surname must contains only letters');
      return false;
    }

    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailPattern.test(this.user.email)) {
      this.toast.error('You must enter a valid email address');
      return false;
    }

    const firmNamePattern = /^[a-zA-Z0-9\s!@#$%^&*(),.?":\/^'{}\|<>=\-]+$/;

    if (this.registrationType === 'company' && this.user.firmName!=null &&!firmNamePattern.test(this.user.firmName)) {
      this.toast.error('Firm name invalid');
      return false;
    }

    const pibPattern = /^[0-9]{8,13}$/;
    if (this.registrationType === 'company' && this.user.pib!=null &&!pibPattern.test(this.user.pib)) {
      this.toast.error('Pib must contains only digits, exactly 10 digits');
      return false;
    }

    const streetPattern = /^[a-zA-Z0-9\s]+$/;
    if (!streetPattern.test(this.street)) {
      this.toast.error('Street must contains only letters and digits');
      return false;
    }

    const statePattern = /^[a-zA-Z\s]+$/;
    if (!statePattern.test(this.state)) {
      this.toast.error('State must contains only letters');
      return false;
    }

    const cityPattern = /^[a-zA-Z\s]+$/;
    if (!cityPattern.test(this.city)) {
      this.toast.error('City must contains only letters');
      return false;
    }

    const phonePattern = /^[0-9]{8,10}$/;
    if (!phonePattern.test(this.user.phone)) {
      this.toast.error('Phone number must contains only digits, exactly 8-10 digits');
      return false;
    }

    const passwordPattern = /^(?=.*[A-Z])(?=.*[@$!%*?&])(?=.*[a-z]).{8,}$/;
    if (!passwordPattern.test(this.user.password)) {
      this.toast.error(
        'Password must contain lowercase and uppercase letter, special character, and be minimum 8 characters long'
      );
      return false;
    }

    if (this.user.password !== this.confirmPassword) {
      this.toast.error('Passwords do not match');
      return false;
    }

    return true;
  }

  resetFields() {
    if (this.registrationType === 'individual') {
      this.user = {
        name: '',
        surname: '',
        firmName: null, 
        pib: null,
        phone:'',
        email: '',
        password: '',
        confirmpassword: '',
        address: null,
        packageType:null,
      };
    } else if (this.registrationType === 'company') {
      this.user = {
        name: null,
        surname: null,
        firmName: '',
        pib: '',
        phone:'',
        email: '',
        password: '',
        confirmpassword: '',
        address: null,
        packageType:null,
      };
    }
    this.street = '';
    this.city = '';
    this.state = '';
    this.confirmPassword = '';
  }
  
}
