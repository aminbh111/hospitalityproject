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
        path: 'event',
        data: { pageTitle: 'hospitalityprojectApp.event.home.title' },
        loadChildren: () => import('./event/event.module').then(m => m.EventModule),
      },
      {
        path: 'event-data',
        data: { pageTitle: 'hospitalityprojectApp.eventData.home.title' },
        loadChildren: () => import('./event-data/event-data.module').then(m => m.EventDataModule),
      },
      {
        path: 'about-us',
        data: { pageTitle: 'hospitalityprojectApp.aboutUs.home.title' },
        loadChildren: () => import('./about-us/about-us.module').then(m => m.AboutUsModule),
      },
      {
        path: 'about-us-data',
        data: { pageTitle: 'hospitalityprojectApp.aboutUsData.home.title' },
        loadChildren: () => import('./about-us-data/about-us-data.module').then(m => m.AboutUsDataModule),
      },
      {
        path: 'contact-us',
        data: { pageTitle: 'hospitalityprojectApp.contactUs.home.title' },
        loadChildren: () => import('./contact-us/contact-us.module').then(m => m.ContactUsModule),
      },
      {
        path: 'contact-us-data',
        data: { pageTitle: 'hospitalityprojectApp.contactUsData.home.title' },
        loadChildren: () => import('./contact-us-data/contact-us-data.module').then(m => m.ContactUsDataModule),
      },
      {
        path: 'spa',
        data: { pageTitle: 'hospitalityprojectApp.spa.home.title' },
        loadChildren: () => import('./spa/spa.module').then(m => m.SpaModule),
      },
      {
        path: 'spa-data',
        data: { pageTitle: 'hospitalityprojectApp.spaData.home.title' },
        loadChildren: () => import('./spa-data/spa-data.module').then(m => m.SpaDataModule),
      },
      {
        path: 'room-service',
        data: { pageTitle: 'hospitalityprojectApp.roomService.home.title' },
        loadChildren: () => import('./room-service/room-service.module').then(m => m.RoomServiceModule),
      },
      {
        path: 'room-service-data',
        data: { pageTitle: 'hospitalityprojectApp.roomServiceData.home.title' },
        loadChildren: () => import('./room-service-data/room-service-data.module').then(m => m.RoomServiceDataModule),
      },
      {
        path: 'bars',
        data: { pageTitle: 'hospitalityprojectApp.bars.home.title' },
        loadChildren: () => import('./bars/bars.module').then(m => m.BarsModule),
      },
      {
        path: 'bars-data',
        data: { pageTitle: 'hospitalityprojectApp.barsData.home.title' },
        loadChildren: () => import('./bars-data/bars-data.module').then(m => m.BarsDataModule),
      },
      {
        path: 'meeting',
        data: { pageTitle: 'hospitalityprojectApp.meeting.home.title' },
        loadChildren: () => import('./meeting/meeting.module').then(m => m.MeetingModule),
      },
      {
        path: 'meeting-data',
        data: { pageTitle: 'hospitalityprojectApp.meetingData.home.title' },
        loadChildren: () => import('./meeting-data/meeting-data.module').then(m => m.MeetingDataModule),
      },
      {
        path: 'offer',
        data: { pageTitle: 'hospitalityprojectApp.offer.home.title' },
        loadChildren: () => import('./offer/offer.module').then(m => m.OfferModule),
      },
      {
        path: 'offer-data',
        data: { pageTitle: 'hospitalityprojectApp.offerData.home.title' },
        loadChildren: () => import('./offer-data/offer-data.module').then(m => m.OfferDataModule),
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
