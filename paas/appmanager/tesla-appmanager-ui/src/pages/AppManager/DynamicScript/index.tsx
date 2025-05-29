import { DynamicScriptService } from "@/services/appmanager/dynamic-script.service";
import { AppManagerAPI } from "@/services/appmanager/typings";
import { ActionType, PageContainer, ProColumns, ProFormInstance, ProTable } from "@ant-design/pro-components";
import { useIntl } from "@umijs/max";
import { Button, Modal, Space } from "antd";
import { FC, useRef, useState } from "react"

type DynamicScriptCodeState = {
    show: boolean;
    code?: string;
}

const DynamicScript: FC = () => {
    const intl = useIntl();
    const actionRef = useRef<ActionType>();
    const formRef = useRef<ProFormInstance>();

    const [showCode, setShowCode] = useState<DynamicScriptCodeState>({ show: false });

    const columns: ProColumns<AppManagerAPI.DynamicScript>[] = [
        {
            title: "名称",
            dataIndex: 'name'
        },
        {
            title: "类型",
            dataIndex: 'kind',
            width: 160,
        },
        {
            title: "版本",
            dataIndex: 'currentRevision',
            width: 160,
        },
        {
            title: "操作",
            valueType: 'option',
            width: 160,
            render: (_, record) => (
                <Space>
                    <Button onClick={() => {
                        setShowCode({ show: true, code: record.code });
                    }}>
                        查看
                    </Button>
                </Space>
            ),
        }
    ];

    return (
        <PageContainer >
            <ProTable<AppManagerAPI.DynamicScript>
                search={false}
                rowKey="id"
                actionRef={actionRef}
                formRef={formRef}
                columns={columns}
                pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
                request={(params, sorter, filter) => {
                    const queryParam: AppManagerAPI.DynamicScriptQueryParam = {
                        ...params,
                        page: params.current,
                        pageSize: params.pageSize,
                        pagination: true,
                    }
                    return DynamicScriptService.list(queryParam);
                }}
                options={false}
            />

            {showCode.show && <Modal
                width={'50%'}
                open={showCode.show}
                destroyOnHidden={true}
                onOk={() => {
                    setShowCode({ show: false });
                }}
                onCancel={() => {
                    setShowCode({ show: false });
                }}
            >
                <pre>{showCode.code}</pre>
            </Modal>}
        </PageContainer>
    )
}

export default DynamicScript;