import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ViewChild, ElementRef } from '@angular/core';
import { imageFileValidator } from '../uploadValidator';
import { AuthService } from 'src/app/services/auth.service';



@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  imageSrc: string = '';
  isImageLoaded = false;
  @ViewChild('fileInput') fileInput!: ElementRef;

  imageForm!: FormGroup;
  detailsForm!: FormGroup;


  constructor(private fb: FormBuilder, private authSrv:AuthService) { }

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
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      this.imageSrc = reader.result as string;
      this.isImageLoaded = true;
    };

    this.isImageLoaded = true; // Imposta questa variabile a true quando l'immagine viene caricata con successo
    console.log(this.imageForm.valid);
  }

  cancelImage() {
    this.fileInput.nativeElement.value = '';
    this.isImageLoaded = false; // Imposta questa variabile a false quando l'immagine viene cancellata
    console.log(this.imageForm);
  }


  onSubmit() {
    const profilePictureValue = this.imageForm.get('profilePicture')?.value;
    if (this.imageForm.valid && this.detailsForm.valid) {
    const formDataImage = new FormData();
    formDataImage.append('profilePicture', profilePictureValue);

    const details = JSON.stringify(this.detailsForm.value);

    console.log(profilePictureValue);
    console.log(formDataImage, details);

    this.authSrv.uploadUserImage(formDataImage).subscribe(()=>{console.log(formDataImage);
    });
      // Invia 'formData' e 'details' al server
      // Ad esempio, potresti avere due chiamate HTTP separate o unirle in un'unica chiamata, a seconda delle tue esigenze
    }
    return null;
  }

}
