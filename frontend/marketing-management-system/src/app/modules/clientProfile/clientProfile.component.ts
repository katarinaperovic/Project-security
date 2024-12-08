import { Component, OnInit } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/services/auth/auth.service';
import { ClientService } from 'src/app/services/client/client.service';

@Component({
  selector: 'app-client-profile',
  templateUrl: './clientProfile.component.html',
  styleUrls: ['./clientProfile.component.css'],
})
export class ClientProfileComponent implements OnInit {
  public user: any;
  public name: any;
  public surname: any;
  public firmName:any;
  public pib:any;
  public street: any;
  public state: any;
  public city: any;

  constructor(
    private toastr: ToastrService,
    private clientService: ClientService,
    private authService: AuthService,
    private jwtHelper: JwtHelperService
  ) {}

  ngOnInit(): void {
    var token = this.authService.getToken();
    if (token !== null) {
      var userID = this.jwtHelper.decodeToken(token).id;
      this.clientService.getByID(userID).subscribe(
        (res) => {
          this.user = res;
          this.name = this.user.name;
          this.surname = this.user.surname;
          this.firmName = this.user.firmName;
          this.pib = this.user.pib;
          this.street = this.user.address.street;
          this.state = this.user.address.state;
          this.city = this.user.address.city;
        },
        (error) => {
          this.toastr.show('','Not authorized!')
        }
      );
    }
  }

}