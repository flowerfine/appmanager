import { DefinitionSchemaService } from "@/services/appmanager/definition-schema.service";
import { DynamicScriptService } from "@/services/appmanager/dynamic-script.service";
import { AppManagerAPI } from "@/services/appmanager/typings";
import { ActionType, PageContainer, ProColumns, ProFormInstance, ProTable } from "@ant-design/pro-components";
import { useIntl } from "@umijs/max";
import { Button, Modal, Space } from "antd";
import { FC, useRef, useState } from "react"

type DefinitionSchemaState = {
    show: boolean;
    schema?: string;
}

const DefinitionSchema: FC = () => {
    const intl = useIntl();
    const actionRef = useRef<ActionType>();
    const formRef = useRef<ProFormInstance>();

    const [showSchema, setShowSchema] = useState<DefinitionSchemaState>({ show: false });

    const columns: ProColumns<AppManagerAPI.DefinitionSchema>[] = [
        {
            title: "名称",
            dataIndex: 'name'
        },
        {
            title: "操作",
            valueType: 'option',
            width: 160,
            render: (_, record) => (
                <Space>
                    <Button onClick={() => {
                        setShowSchema({ show: true, schema: record.jsonSchema });
                    }}>
                        查看
                    </Button>
                </Space>
            ),
        }
    ];

    return (
        <PageContainer >
            <ProTable<AppManagerAPI.DefinitionSchema>
                search={false}
                rowKey="id"
                actionRef={actionRef}
                formRef={formRef}
                columns={columns}
                pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
                request={(params, sorter, filter) => {
                    const queryParam: AppManagerAPI.DefinitionSchemaQueryParam = {
                        ...params,
                        page: params.current,
                        pageSize: params.pageSize,
                        pagination: true,
                    }
                    return DefinitionSchemaService.list(queryParam);
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
                <pre>{JSON.stringify(JSON.parse(showSchema.schema), null, 2)}</pre>
            </Modal>}
        </PageContainer>
    )
}

export default DefinitionSchema;