import { PluginService } from "@/services/appmanager/plugin.service";
import { AppManagerAPI } from "@/services/appmanager/typings";
import { EditOutlined } from "@ant-design/icons";
import { ActionType, PageContainer, ProColumns, ProFormInstance, ProTable } from "@ant-design/pro-components";
import { useIntl } from "@umijs/max";
import { Alert, Button, Modal, Space, Switch, Tooltip } from "antd";
import { FC, useRef, useState } from "react";

type DefinitionSchemaState = {
  show: boolean;
  schema?: string;
}

const TraitPlugin: FC = () => {
  const intl = useIntl();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();

  const [showSchema, setShowSchema] = useState<DefinitionSchemaState>({ show: false });

  const columns: ProColumns<AppManagerAPI.Plugin>[] = [
    {
      title: "名称",
      dataIndex: 'pluginName'
    },
    {
      title: "描述",
      dataIndex: 'pluginDescription'
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
    },
    {
      title: "操作",
      valueType: 'option',
      width: 160,
      render: (_, record) => (
        <Space>
          <Button onClick={() => {
            setShowSchema({ show: true, schema: record.pluginSchema });
          }}>
            查看
          </Button>
        </Space>
      ),
    }
  ];

  return (
    <PageContainer breadcrumbRender={false} title={false} content={
      <Alert message="Trait说明"
        description="为您的应用程序提供可扩展的运维特性能力"
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
          pluginKind: 'TraitDefinition',
          withBlobs: true
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

      {showSchema.show && <Modal
        width={'50%'}
        open={showSchema.show}
        destroyOnHidden={true}
        onOk={() => {
          setShowSchema({ show: false });
        }}
        onCancel={() => {
          setShowSchema({ show: false });
        }}
      >
        <pre>{showSchema.schema}</pre>
      </Modal>}
    </PageContainer>
  )
}

export default TraitPlugin;
