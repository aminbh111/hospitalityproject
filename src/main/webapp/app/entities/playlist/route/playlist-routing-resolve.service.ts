import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlaylist, Playlist } from '../playlist.model';
import { PlaylistService } from '../service/playlist.service';

@Injectable({ providedIn: 'root' })
export class PlaylistRoutingResolveService implements Resolve<IPlaylist> {
  constructor(protected service: PlaylistService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlaylist> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((playlist: HttpResponse<Playlist>) => {
          if (playlist.body) {
            return of(playlist.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Playlist());
  }
}
