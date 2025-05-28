import { PageContainer } from "@ant-design/pro-components";
import { Alert } from "antd";
import { FC } from "react";

const TraitPlugin: FC = () => {

  

  return (
    <PageContainer breadcrumbRender={false} title={false} content={
      <Alert message="Trait说明"
        description="为您的应用程序提供可扩展的运维特性能力"
        type="warning"
        showIcon
        closable
      />} 
      >

      运维特征插件
    </PageContainer >
  )
}

export default TraitPlugin;
