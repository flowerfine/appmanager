import {PageParam} from "@/typings";

declare namespace AppManagerAPI {

  type App = {
    id: number;
    gmtCreate: Date;
    gmtModified: Date;
    appId: string
    options?: Record<string, any>;
    environments?: Array<AppDeployEnvironment>;
  };

  type AppParam = PageParam & {
    
  };

  type AppDeployEnvironment = {
    clusterId: string
    namespaceId: string
    stageId: string
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
  };

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

  };

  type DefinitionSchema = {
    id: number;
    gmtCreate: Date;
    gmtModified: Date;
    name: string;
    jsonSchema: string;
  };

  type DefinitionSchemaQueryParam = PageParam & {

  };

  type AddonMeta = {
    id: number;
    gmtCreate: Date;
    gmtModified: Date;
    addonType: string;
    addonId: string;
    addonVersion: string;
    addonLabel: string;
    addonDescription: string;
    addonSchema: string;
    componentsSchema: Record<string, any>;
  };

  type AddonMetaQueryParam = PageParam & {

  };

  type Trait = {
    id: number;
    gmtCreate: Date;
    gmtModified: Date;
    name: string;
    className: string;
    label?: string;
    definitionRef: string;
    traitDefinition: string;
  };

  type TraitQueryParam = PageParam & {

  };
}
