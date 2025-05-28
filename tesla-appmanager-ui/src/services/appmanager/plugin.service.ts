import {PageData, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import { AppManagerAPI } from './typings';

export const PluginService = {
  url: '/server/plugins',

  list: async (queryParam: AppManagerAPI.PluginQueryParam) => {
    return request<ResponseBody<PageData<AppManagerAPI.Plugin>>>(`${PluginService.url}`, {
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
