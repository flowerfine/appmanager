import {PageData, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import { AppManagerAPI } from './typings';

export const TraitService = {
  url: '/traits',

  list: async (queryParam: AppManagerAPI.TraitQueryParam) => {
    return request<ResponseBody<PageData<AppManagerAPI.Trait>>>(`${TraitService.url}`, {
      method: 'GET',
      params: queryParam,
    }).then((res) => {
      const result = {
        data: res.data?.items,
        total: res.data?.total,
        pageSize: res.data?.pageSize,
        current: res.data?.page,
      };
      return result;
    });
  },
};
