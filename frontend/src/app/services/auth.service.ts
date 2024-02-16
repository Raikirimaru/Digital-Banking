import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isAuthenticated : boolean = false;
  roles : any
  access_token! : any
  username : any
  baseUrl : string = 'http://localhost:8080'

  constructor(private http : HttpClient) { }

  public signin = (username : string, password : string) => {
    let options = {
      headers : new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    }
    let params = new HttpParams().set("username", username).set("password", password)
    return this.http.post(`${this.baseUrl}/auth/signin`, params, options)
  }

  loadProfile(res: any) {
    this.isAuthenticated = true
    this.access_token = res['access_token']
    let jwtDecoder : any = jwtDecode(this.access_token)
    console.log(jwtDecoder);
    this.username = jwtDecoder.sub
    this.roles = jwtDecoder.scope
  }

  signout =  () => {
    this.isAuthenticated = false
    this.access_token = undefined
    this.username = undefined
    this.roles = undefined
  }
}
