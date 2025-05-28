import {PageData, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import { AppManagerAPI } from './typings';

export const DynamicScriptService = {
  url: '/dynamic-script',

  list: async (queryParam: AppManagerAPI.DynamicScriptQueryParam) => {
    return request<ResponseBody<PageData<AppManagerAPI.DynamicScript>>>(`${DynamicScriptService.url}`, {
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
