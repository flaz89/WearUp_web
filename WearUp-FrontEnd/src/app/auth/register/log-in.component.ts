import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.scss']
})
export class LogInComponent implements OnInit {

  selectedButton: string | null = null;
  hideElements:boolean = false;

  showForm(button: string) {
    this.selectedButton = button;
    this.hideElements = true;
    console.log("hai selezionato il form: " + button);
  }

  getBack() {
    this.selectedButton = null;
    this.hideElements = false;
    console.log("Stato resettato");
  }


  switchToAccess() {
    this.selectedButton = 'access';
  }

  constructor() { }

  ngOnInit(): void {
  }

}
