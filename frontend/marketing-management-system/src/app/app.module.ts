import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './modules/navbar/navbar.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MatCardModule } from '@angular/material/card';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { MatDialogModule } from '@angular/material/dialog';
import { RegisterComponent } from './modules/register/register.component';
import { LoginComponent } from './modules/login/login.component';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';


import { JwtInterceptor, JwtModule } from '@auth0/angular-jwt';
import { PermissionComponent } from './modules/permission/permission.component';
import { ClientProfileComponent } from './modules/clientProfile/clientProfile.component';

import { RECAPTCHA_SETTINGS, RecaptchaFormsModule, RecaptchaModule, RecaptchaSettings } from 'ng-recaptcha';
import { NgxCaptchaModule, ReCaptcha2Component } from 'ngx-captcha';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    RegisterComponent,
    LoginComponent,
    PermissionComponent,
    ClientProfileComponent,
  ],
  imports: [
    MatSlideToggleModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      positionClass: 'toast-top-right',
      preventDuplicates: true,
    }),
    FormsModule,
    BrowserModule,
    NgbModule,
    AppRoutingModule,
    MatCardModule,
    MatDialogModule,
    HttpClientModule,
    RecaptchaFormsModule,
    
    RecaptchaModule,
    JwtModule.forRoot({
      config: {
        tokenGetter: () => localStorage.getItem('jwt-token'),
      },
    }),
    NgxCaptchaModule,
  ],
  
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true,
    },
    {
      provide: RECAPTCHA_SETTINGS,
      useValue: {
        siteKey: '6Lco4N4pAAAAAD2yK1LL8Nhzdb3srd9WFcS1LfRD'
      } as RecaptchaSettings
    },
  ],

  bootstrap: [AppComponent],
})
export class AppModule {}
