import { bootstrapApplication } from '@angular/platform-browser';
import { importProvidersFrom } from '@angular/core';
import {AppComponent} from './app/app.component';
import {AppRoutingModule} from './app/app.routes';
import {provideHttpClient} from '@angular/common/http';

bootstrapApplication(AppComponent, {
  providers: [importProvidersFrom(AppRoutingModule), provideHttpClient()]
});
