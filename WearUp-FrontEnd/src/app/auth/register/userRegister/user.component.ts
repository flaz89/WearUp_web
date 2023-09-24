import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ViewChild, ElementRef } from '@angular/core';
import { imageFileValidator } from '../uploadValidator';
import { AuthService } from 'src/app/services/auth.service';
import { UserRegister } from 'src/app/model/UserRegister.interface';
import { Router } from '@angular/router';



@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  imageSrc: string = '';
  isImageLoaded:boolean = false;
  @ViewChild('fileInput') fileInput!: ElementRef;

  @Output() registrationSuccess = new EventEmitter<void>();

  imageForm!: FormGroup;
  detailsForm!: FormGroup;
  selectedUserImage: File | null = null;

  isLoading:boolean = false;
  serverMessageOk!:string;
  serverMessageError!:string;


  constructor(private fb: FormBuilder, private authSrv:AuthService, private router: Router) { }

  ngOnInit(): void {
    this.imageForm = this.fb.group({
      profilePicture: ['' || null,[imageFileValidator]]
    });

    this.detailsForm = this.fb.group({
      name: ['', Validators.required],
      surname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  imageUploaded(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedUserImage = file;
      console.log(file);


      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.imageSrc = reader.result as string;
        this.isImageLoaded = true;
      };
    }
  }

  cancelImage() {
    this.fileInput.nativeElement.value = '';
    this.imageForm.get("profilePicture")?.reset;
    this.isImageLoaded = false;
    this.imageForm.reset();
  }



  onSubmit() {
    this.isLoading = true;
    if (this.imageForm.valid && this.detailsForm.valid) {
      if (this.selectedUserImage) {
        const formDataImage = new FormData();
        formDataImage.append('file', this.selectedUserImage);
        console.log(formDataImage);


        this.authSrv.uploadUserImage(formDataImage).subscribe((response: any) => {
          // Ottieni l'URL dell'immagine caricata dal server
          const uploadedImageUrl = response.url;
          // Assegna l'URL all'oggetto UserRegister
          const userRegisterData: UserRegister = {
            ...this.detailsForm.value,
            profilePicture: uploadedImageUrl
          };
          // Ora puoi effettuare la chiamata POST per registrare l'utente
          this.authSrv.registerUser(userRegisterData).subscribe(response => {
            this.isLoading = false;
            this.serverMessageOk = "User saved successfully";
            console.log(response);
            setTimeout(() => {
              this.registrationSuccess.emit();
            }, 1000);

          }, error => {
            this.isLoading = false;
            this.serverMessageError = error.error.message;
            console.log(response.error.message);
          });

        }, error => {
          this.isLoading = false;
          this.serverMessageError = "Error loading image";
            console.log(error);
        });
      } else {
        // Caso in cui non viene caricata un'immagine
        const userRegisterData: UserRegister = {
          ...this.detailsForm.value
        };
        // Effettua la chiamata POST come al solito
        this.authSrv.registerUser(userRegisterData).subscribe(response => {
          this.isLoading = false;
          this.serverMessageOk = "User saved successfully";
            console.log(response);
            setTimeout(() => {
              this.registrationSuccess.emit();;
            }, 1000);
        }, error => {
          this.isLoading = false;
          this.serverMessageError = error.error.message;
          console.log(error.error.message);
        });
      }
    }
  }

}
