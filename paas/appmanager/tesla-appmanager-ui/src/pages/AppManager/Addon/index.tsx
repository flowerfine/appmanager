import { AddonMetaService } from "@/services/appmanager/addon-meta.service";
import { AppManagerAPI } from "@/services/appmanager/typings";
import { ActionType, PageContainer, ProColumns, ProFormInstance, ProTable } from "@ant-design/pro-components";
import { useIntl } from "@umijs/max";
import { Button, Modal, Space } from "antd";
import { FC, useRef, useState } from "react"

type AddonMetaSchemaState = {
    show: boolean;
    schema?: string;
}

const Addon: FC = () => {
    const intl = useIntl();
    const actionRef = useRef<ActionType>();
    const formRef = useRef<ProFormInstance>();

    const [showSchema, setShowSchema] = useState<AddonMetaSchemaState>({ show: false });

    const columns: ProColumns<AppManagerAPI.AddonMeta>[] = [
        {
            title: "名称",
            dataIndex: 'addonLabel'
        },
        {
            title: "类型",
            dataIndex: 'addonType'
        },
        {
            title: "描述",
            dataIndex: 'addonDescription'
        },
        {
            title: "版本",
            dataIndex: 'addonVersion'
        },
        {
            title: "Schema",
            valueType: 'option',
            width: 160,
            render: (_, record) => (
                <Space>
                    <Button onClick={() => {
                        setShowSchema({ show: true, schema: record.addonSchema });
                    }}>
                        查看
                    </Button>
                </Space>
            ),
        }
    ];

    return (
        <PageContainer >
            <ProTable<AppManagerAPI.AddonMeta>
                search={false}
                rowKey="id"
                actionRef={actionRef}
                formRef={formRef}
                columns={columns}
                pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
                request={(params, sorter, filter) => {
                    const queryParam: AppManagerAPI.AddonMetaQueryParam = {
                        ...params,
                        page: params.current,
                        pageSize: params.pageSize,
                        pagination: true,
                    }
                    return AddonMetaService.list(queryParam);
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

export default Addon;