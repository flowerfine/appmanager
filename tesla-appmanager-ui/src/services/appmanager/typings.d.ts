declare namespace AppManagerAPI {

  export type ResponseBody<T> = {
    code?: number;
    message?: string;
    requestId?: string;
    timestamp?: number;
    data?: T;
  };

  export type PageData<T> = {
    pageSize: number;
    page: number;
    total: number;
    empty: boolean;
    items: T[];
  };

  export type PageParam = {
    pageSize?: number;
    page?: number;
    pagination?: boolean;
    withBlobs?: boolean;
  };

  type Plugin = {
    id: number;
    gmtCreate: Date;
    gmtModified: Date;
    pluginKind: string
    pluginName: string;
    pluginVersion: string;
    pluginRegistered: boolean;
    packagePath: string;
    pluginDescription?: string;
    pluginDependencies?: string;
    pluginSchema?: string;
  };

  type PluginQueryParam = PageParam & {
    pluginKind: string;
  }
}
