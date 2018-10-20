import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import {Observable} from 'rxjs';

export class ServerInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const url = 'http://192.168.1.118:8080';
    req = req.clone({
      url: url + req.url
    });
    return next.handle(req);
  }
}
