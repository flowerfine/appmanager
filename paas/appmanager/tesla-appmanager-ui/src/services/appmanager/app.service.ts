import {PageData, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import { AppManagerAPI } from './typings';

export const AppService = {
  url: '/appmanager/apps',

  list: async (queryParam: AppManagerAPI.AppParam) => {
    return request<ResponseBody<PageData<AppManagerAPI.App>>>(`${AppService.url}`, {
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
