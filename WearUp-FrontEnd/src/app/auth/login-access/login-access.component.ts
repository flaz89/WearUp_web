import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccessLogin } from 'src/app/model/AccessLogin.interface';
import { AuthService } from 'src/app/services/auth.service';
import { JwtHelperService } from '@auth0/angular-jwt';


@Component({
  selector: 'app-login-access',
  templateUrl: './login-access.component.html',
  styleUrls: ['./login-access.component.scss']
})
export class LoginAccessComponent implements OnInit {

  public userLoggedIn = false;

  detailsForm!: FormGroup;
  isLoading:boolean = false;

  profilePictureUrl!:string;

  serverMessageOk!:string;
  serverMessageError!:string;

  constructor(private fb: FormBuilder, private authSrv:AuthService, private router: Router, private http:HttpClient, private jwtHelper: JwtHelperService) { }

  ngOnInit(): void {
    this.detailsForm = this.fb.group({
      email: ['', Validators.required],
      password: ['',[Validators.required, Validators.minLength(6)]]
    });
  }

  async onSubmit() {
    this.isLoading = true;

    if (this.detailsForm.valid) {
      const loginData: AccessLogin = {
        email: this.detailsForm.get('email')?.value,
        password: this.detailsForm.get('password')?.value
      };

      try {
        const response: any = await this.authSrv.login(loginData).toPromise();

          if (response && response.accessToken) {
            let userName:String = "";

            const decodedToken = this.jwtHelper.decodeToken(response.accessToken);
            const userType = decodedToken['entityType'];
            const userId =  decodedToken["sub"];
            const headers = new HttpHeaders().set('Authorization', `Bearer ${response.accessToken}`);

            try {

              if(userType == "User") {
                const user: any = await this.http.get("http://localhost:3001/users/" + userId, { headers }).toPromise();
                userName = user.name;
                this.profilePictureUrl = user.profilePicture;
                this.authSrv.updateProfilePicture(this.profilePictureUrl);
                console.log(user);
              }

              if(userType == "Admin") {
                const admin: any = await this.http.get("http://localhost:3001/users/" + userId, { headers }).toPromise();
                userName = "ADMIN";
                this.profilePictureUrl = admin.profilePicture;
                this.authSrv.updateProfilePicture(this.profilePictureUrl);
                console.log(admin);
              }

              if (userType == "Brand"){
                const brand: any = await this.http.get("http://localhost:3001/brands/" + userId, { headers }).toPromise();
                userName = brand.brandName
                this.profilePictureUrl = brand.profilePicture;
                this.authSrv.updateProfilePicture(this.profilePictureUrl);
                console.log(brand);
              }

            } catch (error:any) {
              this.serverMessageError = error.message;
              console.error(error);
            }

            this.serverMessageOk = "Welcome back " + userName + "!";
            setTimeout(() => {
              this.router.navigate(['/products']);
            }, 2000);
          } else {
            this.serverMessageError = "Any token found!"
          }

      } catch (error:any) {
        this.serverMessageError = error.error.message;
        console.error("Errore durante il login:", error);
         // Gestione dell'errore
      } finally {
        this.isLoading = false;
      }
    }
  }
}
