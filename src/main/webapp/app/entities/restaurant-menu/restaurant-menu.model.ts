import dayjs from 'dayjs/esm';
import { IRestaurantMenuData } from 'app/entities/restaurant-menu-data/restaurant-menu-data.model';
import { Position } from 'app/entities/enumerations/position.model';

export interface IRestaurantMenu {
  id?: number;
  date?: dayjs.Dayjs | null;
  publish?: boolean | null;
  contentPosition?: Position | null;
  imagePosition?: Position | null;
  restaurantMenuData?: IRestaurantMenuData[] | null;
}

export class RestaurantMenu implements IRestaurantMenu {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public publish?: boolean | null,
    public contentPosition?: Position | null,
    public imagePosition?: Position | null,
    public restaurantMenuData?: IRestaurantMenuData[] | null
  ) {
    this.publish = this.publish ?? false;
  }
}

export function getRestaurantMenuIdentifier(restaurantMenu: IRestaurantMenu): number | undefined {
  return restaurantMenu.id;
}
