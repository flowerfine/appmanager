import {PageParam} from "@/typings";

declare namespace AppManagerAPI {

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

  type DynamicScript = {
    id: number;
    gmtCreate: Date;
    gmtModified: Date;
    kind: string
    name: string;
    code: string;
    currentRevision: number;
    envId: string;
  };

  type DynamicScriptQueryParam = PageParam & {

  }
}
