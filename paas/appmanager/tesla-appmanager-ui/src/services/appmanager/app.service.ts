import { PageData, ResponseBody } from '@/typings';
import { request } from '@umijs/max';
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

  listVersions: async (appId: string) => {
    return request<ResponseBody<PageData<AppManagerAPI.AppVersion>>>(`${AppService.url}/${appId}/versions`, {
      method: 'GET',
    }).then((res) => {
      const result = {
        data: res.data
      };
      return result;
    });
  },

  listComponents: async (appId: string) => {
    return request<ResponseBody<PageData<AppManagerAPI.AppComponent>>>(`${AppService.url}/${appId}/components`, {
      method: 'GET',
    }).then((res) => {
      const result = {
        data: res.data
      };
      return result;
    });
  },

    listPackageTasks: async (appId: string) => {
    return request<ResponseBody<PageData<AppManagerAPI.AppPackageTask>>>(`${AppService.url}/${appId}/app-package-tasks`, {
      method: 'GET',
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

  listConfigurationTypes: async (appId: string) => {
    return request<ResponseBody<PageData<AppManagerAPI.AppConfiguration>>>(`/appmanager/application-configurations/types`, {
      method: 'GET',
      params: { appId: appId },
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
