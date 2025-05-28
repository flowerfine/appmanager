import {request} from '@umijs/max';
import { AppManagerAPI } from './typings';

export const PluginService = {
  url: '/api/plugins',

  list: async (queryParam: AppManagerAPI.PluginQueryParam) => {
    return request<AppManagerAPI.ResponseBody<AppManagerAPI.PageData<AppManagerAPI.Plugin>>>(`${PluginService.url}`, {
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
