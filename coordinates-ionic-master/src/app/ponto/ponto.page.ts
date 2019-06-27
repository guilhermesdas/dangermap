import { Component, OnInit } from '@angular/core';
import { WsPontosService } from '../ws-pontos.service';
import { Ponto } from '../ponto';
import { User } from '../user';
import { NavController, ToastController, Platform, LoadingController, AlertController } from '@ionic/angular';
import { Router, ActivatedRoute } from '@angular/router';
import { faCompass, faInfoCircle, faChevronCircleLeft, faMapMarker, faPhone, faRecycle, faDesktop, faUser } from '@fortawesome/free-solid-svg-icons';
import { Geolocation, Geoposition } from '@ionic-native/geolocation/ngx';
import { NativeGeocoder, NativeGeocoderReverseResult, NativeGeocoderForwardResult, NativeGeocoderOptions } from '@ionic-native/native-geocoder/ngx';
import { Location } from '@angular/common';
import { AuthGuardService } from '../auth-guard.service';
import { Type } from '../type';
import { promises } from 'fs';
import { map, filter, scan, finalize } from 'rxjs/operators';
import { prependListener } from 'cluster';
import { resolve } from 'path';
import { Storage } from '@ionic/storage';

declare var google;

@Component({
  selector: 'app-ponto',
  templateUrl: './ponto.page.html',
  styleUrls: ['./ponto.page.scss'],
})

export class PontoPage implements OnInit {
  public pt: {
    _id: String,
    description: String,
    icon: String,
    files: String[],
    username: String
  } = {_id: '', description: '', icon: '', files: [''], username: '...'};

  tipos: Type[] = [];
  users: User[] = [];
  pontoUser;
  pontoLocation;
  tipo;
  ponto: Ponto;

  public urlBase = '';

  faCompass = faCompass;
  faInfoCircle = faInfoCircle;
  faUser = faUser;
  faChevronCircleLeft = faChevronCircleLeft;
  faMapMarker = faMapMarker;
  faPhone = faPhone;
  faRecycle = faRecycle;
  faDesktop = faDesktop;

  myLatLng: {
    lat: number,
    lng: number
  };

  distance = null;

  constructor(
    public wspontos: WsPontosService,
    public navCtrl: NavController,
    private activatedRoute: ActivatedRoute,
    private geolocation: Geolocation,
    private nativeGeocoder: NativeGeocoder,
    private _location: Location,
    private toastController: ToastController,
    private platform: Platform,
    private loadingCtrl: LoadingController,
    public global: AuthGuardService,
    private alertCtrl: AlertController,
    private storage: Storage
  ) { }

  ngOnInit() {
    this.platform.ready().then(async () => {

      // this.nativeGeocoder.reverseGeocode(52.5072095, 13.1452818, {useLocale: true, maxResults: 5})
      //         .then((result: NativeGeocoderReverseResult[]) => this.presentToast(JSON.stringify(result[0])))
      //         .catch((error: any) => this.presentToast(error));

      const loading = await this.loadingCtrl.create({
        message: 'User was added successfully',
        duration: 3000,
      });
    
      loading.present();

      if (this.global.getUser) {
        this.urlBase = this.wspontos.urlBase;
        // var p0 = new Promise(async (resolve,reject)=>{
          
          this.users = this.global.getUsers()
          if (this.users == undefined) {
            this.wspontos.getUsers()
            .pipe(
              filter((p) => p !== undefined)
            )
            .subscribe(data => {
              this.global.setUsers(data)
              this.users = data
            })
          }
        
        
        this.wspontos.getTypes()
        .pipe(
          finalize(()=> {
            this.wspontos.getPonto(this.activatedRoute.snapshot.paramMap.get('id'))
            .pipe(
              finalize(()=>loading.dismiss())
            )
            .subscribe(data => {
              this.ponto = data;
              this.tipo = this.tipos.find(y => y._id == data.typeId);
              this.pontoUser = this.users.find(y => y._id == data.userId);
              console.log('data.userId: '+data.userId);
              console.log('pontoUser.login: '+this.pontoUser.login);
              
              // this.pontoLocation = this.geocodeLatLng(data.lat,data.lng);

              this.nativeGeocoder.reverseGeocode(parseFloat(data.lat), parseFloat(data.lng), {useLocale: true, maxResults: 5})
              .then((result: NativeGeocoderReverseResult[]) => {
                //this.presentToast(result[0].thoroughfare+' '+result[0].subThoroughfare)
                this.pontoLocation = result[0].thoroughfare+', '+result[0].subThoroughfare+', '+result[0].postalCode;
              })
              .catch((error: any) => this.presentToast(error));


              //console.log("this.pontoLocation: "+this.pontoLocation);
            
              if (this.tipo != undefined && this.pontoUser != undefined) {
                this.pt = {
                  _id: data._id,
                  description: this.tipo.description,
                  icon: this.tipo.icon,
                  files: data.files,
                  username: this.pontoUser.login
                }
              } else {
                this.presentToast("Não foi possível carregar este Ponto.");
                this.navCtrl.navigateForward('/pontos');
              }
              
        
              var p1 = new Promise(async (resolve,reject)=>{
                //this.myLatLng = await this.getLocation();
                this.myLatLng = this.global.getLocation();
                if (this.myLatLng == undefined) {
                  this.myLatLng = await this.getLocation();
                  this.global.setLocation(this.myLatLng)
                }
                resolve(this.myLatLng);
                // reject(window.location.reload());
              });  
              p1.then(async (value: {lat:number, lng:number})=>{
                  // console.log("value: "+JSON.stringify(value));
                  // console.log("ponto: "+JSON.stringify(data));
                  // console.log("lat lng: "+data.lat+" "+data.lng);
                  // this.distance = await this.geodesicDistance(+data.lat,+data.lng);
                  // console.log("distance: "+this.distance);
                  var p2 = new Promise(async(sucess,fail)=>{
                    var distancia = this.geodesicDistance(+data.lat,+data.lng,+value.lat,+value.lng);
                    sucess(distancia);
                  });
                  p2.then((result)=>{
                    console.log("value:"+result);
                    
                    if (+result > 1000) {
                      let d = new Intl.NumberFormat('pt-br', {maximumFractionDigits: 2, minimumFractionDigits: 0}).format((+result/1000));
                      this.distance = d+'k';
                    } else {
                      let d = new Intl.NumberFormat('pt-br', {maximumFractionDigits: 2, minimumFractionDigits: 0}).format(+result);
                      this.distance = d;
                    }
                    
                  });
              });
            });
          })
        )
        .subscribe(types => {
          this.tipos = types;
        });
      } else {
        this.goToMapa();      
      }
    })
  }

  goBack() {
    // this.navCtrl.navigateBack;
    this._location.back();
  }
  
  async presentToast(text) {
    const toast = await this.toastController.create({
      message: text,
      position: 'bottom',
      duration: 2000
    });
    toast.present();
  }

  private async getLocation() {
    const rta = await this.geolocation.getCurrentPosition();
    return {
      lat: rta.coords.latitude,
      lng: rta.coords.longitude
    };
  }

  private geodesicDistance(lat1: number,lng1: number,lat2: number,lng2: number) {
    // console.log("lat lng: "+lat1+" "+lng1);
    var R = 6371000; // metres
    var φ1 = this.toRad(lat1);
    var φ2 = this.toRad(+lat2);
    var Δφ = Math.sqrt(Math.pow(this.toRad(+lat2)-this.toRad(lat1),2));
    var Δλ = Math.sqrt(Math.pow(this.toRad(+lng2)-this.toRad(lng1),2));
    var a = Math.sin(Δφ/2)*Math.sin(Δφ/2)+Math.cos(φ1)*Math.cos(φ2)*Math.sin(Δλ/2)*Math.sin(Δλ/2);
    var c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    var d = (R * c).toFixed(1);
    // console.log("φ1: "+φ1);
    // console.log("φ2: "+φ2);
    // console.log("Δφ: "+Δφ);
    // console.log("Δλ: "+Δλ);
    // console.log("c: "+c);
    return d;
  }

  public toRad(value: number) {
    return value * Math.PI / 180;
  }

  private goToMapa() {
    this.navCtrl.navigateForward('/mapa');
  }
  private async logout() {
    const alertLogout = await this.alertCtrl.create({
      backdropDismiss: false,
      message: '<b>'+this.global.getUser().username+'</b>, deseja realmente sair?',
      buttons: [
        {
          text: 'cancelar',
          role: 'cancel',
          cssClass: 'alert-cancel',
          
        },
        {
          text: 'sair',
          handler: (data) => {
                this.global.setUser(null);
                this.storage.set('cico',null);
                this.goToMapa();
          }
        }
      ]
    });

    return await alertLogout.present(); 
  }
}