import {PageData, ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import { AppManagerAPI } from './typings';

export const DefinitionSchemaService = {
  url: '/definition-schemas',

  list: async (queryParam: AppManagerAPI.DefinitionSchemaQueryParam) => {
    return request<ResponseBody<PageData<AppManagerAPI.DefinitionSchema>>>(`${DefinitionSchemaService.url}`, {
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
