import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'service',
        data: { pageTitle: 'hospitalityprojectApp.service.home.title' },
        loadChildren: () => import('./service/service.module').then(m => m.ServiceModule),
      },
      {
        path: 'service-data',
        data: { pageTitle: 'hospitalityprojectApp.serviceData.home.title' },
        loadChildren: () => import('./service-data/service-data.module').then(m => m.ServiceDataModule),
      },
      {
        path: 'restaurant-menu',
        data: { pageTitle: 'hospitalityprojectApp.restaurantMenu.home.title' },
        loadChildren: () => import('./restaurant-menu/restaurant-menu.module').then(m => m.RestaurantMenuModule),
      },
      {
        path: 'restaurant-menu-data',
        data: { pageTitle: 'hospitalityprojectApp.restaurantMenuData.home.title' },
        loadChildren: () => import('./restaurant-menu-data/restaurant-menu-data.module').then(m => m.RestaurantMenuDataModule),
      },
      {
        path: 'concierge',
        data: { pageTitle: 'hospitalityprojectApp.concierge.home.title' },
        loadChildren: () => import('./concierge/concierge.module').then(m => m.ConciergeModule),
      },
      {
        path: 'concierge-data',
        data: { pageTitle: 'hospitalityprojectApp.conciergeData.home.title' },
        loadChildren: () => import('./concierge-data/concierge-data.module').then(m => m.ConciergeDataModule),
      },
      {
        path: 'playlist',
        data: { pageTitle: 'hospitalityprojectApp.playlist.home.title' },
        loadChildren: () => import('./playlist/playlist.module').then(m => m.PlaylistModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
