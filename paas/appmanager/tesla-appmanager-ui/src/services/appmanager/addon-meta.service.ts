import {PageData, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import { AppManagerAPI } from './typings';

export const AddonMetaService = {
  url: '/addon',

  list: async (queryParam: AppManagerAPI.AddonMetaQueryParam) => {
    return request<ResponseBody<PageData<AppManagerAPI.AddonMeta>>>(`${AddonMetaService.url}`, {
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
