import { AddonMetaService } from "@/services/appmanager/addon-meta.service";
import { TraitService } from "@/services/appmanager/trait.service";
import { AppManagerAPI } from "@/services/appmanager/typings";
import { ActionType, PageContainer, ProColumns, ProFormInstance, ProTable } from "@ant-design/pro-components";
import { useIntl } from "@umijs/max";
import { Button, Modal, Space } from "antd";
import { FC, useRef, useState } from "react"

type TraitDefinitionState = {
    show: boolean;
    definition?: string;
}

const Trait: FC = () => {
    const intl = useIntl();
    const actionRef = useRef<ActionType>();
    const formRef = useRef<ProFormInstance>();

    const [showDefinition, setShowDefinition] = useState<TraitDefinitionState>({ show: false });

    const columns: ProColumns<AppManagerAPI.Trait>[] = [
        {
            title: "名称",
            dataIndex: 'name'
        },
        {
            title: "类型",
            dataIndex: 'className'
        },
        {
            title: "引用Definition",
            dataIndex: 'definitionRef'
        },
        {
            title: "Definition",
            valueType: 'option',
            width: 160,
            render: (_, record) => (
                <Space>
                    <Button onClick={() => {
                        setShowDefinition({ show: true, definition: record.traitDefinition });
                    }}>
                        查看
                    </Button>
                </Space>
            ),
        }
    ];

    return (
        <PageContainer >
            <ProTable<AppManagerAPI.Trait>
                search={false}
                rowKey="id"
                actionRef={actionRef}
                formRef={formRef}
                columns={columns}
                pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
                request={(params, sorter, filter) => {
                    const queryParam: AppManagerAPI.TraitQueryParam = {
                        ...params,
                        page: params.current,
                        pageSize: params.pageSize,
                        pagination: true,
                        withBlobs: true
                    }
                    return TraitService.list(queryParam);
                }}
                options={false}
            />

            {showDefinition.show && <Modal
                width={'50%'}
                open={showDefinition.show}
                destroyOnHidden={true}
                onOk={() => {
                    setShowDefinition({ show: false });
                }}
                onCancel={() => {
                    setShowDefinition({ show: false });
                }}
            >
                <pre>{showDefinition.definition}</pre>
            </Modal>}
        </PageContainer>
    )
}

export default Trait;