import { PluginService } from "@/services/appmanager/plugin.service";
import { AppManagerAPI } from "@/services/appmanager/typings";
import { EditOutlined } from "@ant-design/icons";
import { ActionType, PageContainer, ProColumns, ProFormInstance, ProTable } from "@ant-design/pro-components";
import { useIntl } from "@umijs/max";
import { Alert, Button, Space, Switch, Tooltip } from "antd";
import { FC, useRef } from "react";

const ComponentPlugin: FC = () => {
  const intl = useIntl();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();

  const columns: ProColumns<AppManagerAPI.Plugin>[] = [
    {
      title: "名称",
      dataIndex: 'pluginName'
    },
    {
      title: "版本",
      dataIndex: 'pluginVersion',
      width: 160,
    },
    {
      title: "启用",
      dataIndex: 'pluginRegistered',
      valueType: 'switch',
      render: (_, record) => (
        <Switch checked={record.pluginRegistered} />
      ),
      width: 160,
    }
  ];

  return (
    <PageContainer breadcrumbRender={false} title={false} content={
      <Alert message="Component说明"
      description="为您的应用程序提供可扩展的工作负载(workload)组件，内置提供通用微服务组件、helm组件，您也可以自己扩展更多的workload类型"
      type="warning"
      showIcon
      closable
      />}
      >
      <ProTable<AppManagerAPI.Plugin>
        search={false}
        rowKey="id"
        actionRef={actionRef}
        formRef={formRef}
        columns={columns}
        pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
        params={{
          pluginKind: 'ComponentDefinition',
        }}
        request={(params, sorter, filter) => {
          const queryParam: AppManagerAPI.PluginQueryParam = {
            ...params,
            page: params.current,
            pageSize: params.pageSize,
            pagination: true,
          }
          return PluginService.list(queryParam);
        }}
        options={false}
      />
    </PageContainer>
  )
}

export default ComponentPlugin;
