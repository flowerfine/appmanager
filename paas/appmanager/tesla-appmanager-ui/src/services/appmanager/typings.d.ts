import { PageParam } from "@/typings";

declare namespace AppManagerAPI {

  type App = {
    id: number;
    gmtCreate: Date;
    gmtModified: Date;
    appId: string;
    options?: Record<string, any>;
    environments?: Array<AppDeployEnvironment>;
  };

  type AppParam = PageParam & {

  };

  type AppDeployEnvironment = {
    clusterId: string;
    namespaceId: string;
    stageId: string;
  };

  type AppVersion = {
    id: number;
    gmtCreate: Date;
    gmtModified: Date;
    appId: string;
    version: string;
    versionLabel: string;
    versionProperties: Record<string, any>;
    isGlobal: boolean;
  };

  type AppComponent = {
    id: number;
    gmtCreate: Date;
    gmtModified: Date;
    compatible: boolean;
    namespaceId: string;
    stageId: string;
    appId: string;
    category: string;
    componentType: string;
    componentName: string;
    pluginVersion: string;
    config: Record<string, any>;
    typeId: string;
  };

  type AppConfiguration = {
    id: number;
    gmtCreate: Date;
    gmtModified: Date;
    appId: string
    typeId: string;
    envId: string;
    namespaceId: string;
    stageId: string;
    productId: string;
    releaseId: string;
    apiVersion: string;
    currentRevision: number;
    enabled: boolean;
    config: string;
    inherit: boolean;
    configJson: Record<string, any>;
  };

  type AppPackageTask = {
    id: number;
    gmtCreate: Date;
    gmtModified: Date;
    appId: string;
    appPackageId: number;
    packageCreator: string;
    taskStatus: string;
    packageVersion: string;
    simplePackageVersion: string;
    packageOptions: string;
    isOnSale: boolean;
    envId: string;
    namespaceId: string;
    stageId: string;
    swapp: string;
    tags: Array<string>;
  };

  type DeployApp = {
    id: number;
    gmtCreate: Date;
    gmtModified: Date;
    readableGmtCreate: string;
    readableGmtModified: string;
    appPackageId: number;
    packageVersion: string;
    simplePackageVersion: string;
    appId: string;
    clusterId: string;
    namespaceId: string;
    stageId: string;
    gmtStart: Date;
    gmtEnd: Date;
    readableGmtStart: string;
    readableGmtEnd: string;
    cost: string;
    deployStatus: string;
    deployErrorMessage: string;
    deployCreator: string;
    deployAppConfiguration: string;
    deployProcessId: number;
    configSha256: string;
    deployComponents: Array<DeployComponent>;
  };

  type DeployComponent = {
    id: number;
    gmtCreate: Date;
    gmtModified: Date;
    readableGmtCreate: string;
    readableGmtModified: string;
    deployId: number;
    deployType: string;
    identifier: string;
    appId: string;
    clusterId: string;
    namespaceId: string;
    stageId: string;
    gmtStart: Date;
    gmtEnd: Date;
    readableGmtStart: string;
    readableGmtEnd: string;
    cost: string;
    deployStatus: string;
    deployErrorMessage: string;
    deployCreator: string;
    deployProcessId: number;
  };

  type DeployAppParam = PageParam & {
    deployAppIdList: Array<number>;
    appId: string;
    clusterId: string;
    namespaceId: string;
    stageId: string;
    deployStatus: string;
    packageVersion: string;
    stageIdWhiteList: Array<string>;
    stageIdBlackList: Array<string>;
    optionKey: string;
    optionValue: string;
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
