import { PluginService } from "@/services/appmanager/plugin.service";
import { EditOutlined } from "@ant-design/icons";
import { ActionType, PageContainer, ProColumns, ProFormInstance, ProTable } from "@ant-design/pro-components";
import { useIntl } from "@umijs/max";
import { Alert, Button, Space, Tooltip } from "antd";
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
      width: 160,
    },
    {
      title: "操作",
      dataIndex: 'actions',
      valueType: 'option',
      align: 'center',
      width: 160,
      fixed: 'right',
      render: (_, record) => (
        <Space>
          <Tooltip title={intl.formatMessage({ id: 'app.common.operate.edit.label' })}>
            <Button
              shape="default"
              type="link"
              icon={<EditOutlined />}
            />
          </Tooltip>
        </Space>
      ),
    },
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
          const queryParam = {
            ...params,
            page: params.current,
            pageSize: params.pageSize,
            pagination: true,
          }
          return PluginService.list(queryParam);
        }}
        options={false}
        toolbar={{
          actions: [
            <Button
              key="new"
              type="primary"
              onClick={() => {
                
              }}
            >
              上传运维特征
            </Button>
          ],
        }}
      />
    </PageContainer>
  )
}

export default ComponentPlugin;
