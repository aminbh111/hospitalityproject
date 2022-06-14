import { IRestaurantMenu } from 'app/entities/restaurant-menu/restaurant-menu.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IRestaurantMenuData {
  id?: number;
  lang?: Language | null;
  title?: string | null;
  content?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  restaurantMenu?: IRestaurantMenu | null;
}

export class RestaurantMenuData implements IRestaurantMenuData {
  constructor(
    public id?: number,
    public lang?: Language | null,
    public title?: string | null,
    public content?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public restaurantMenu?: IRestaurantMenu | null
  ) {}
}

export function getRestaurantMenuDataIdentifier(restaurantMenuData: IRestaurantMenuData): number | undefined {
  return restaurantMenuData.id;
}
