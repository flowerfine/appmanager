import { AppService } from "@/services/appmanager/app.service";
import { AppManagerAPI } from "@/services/appmanager/typings";
import { ActionType, PageContainer, ProColumns, ProFormInstance, ProTable, useIntl } from "@ant-design/pro-components"
import { Button, Modal, Space, Switch } from "antd";
import { FC, useRef, useState } from "react"

type AppSwappState = {
    show: boolean;
    swapp?: string;
}

const App: FC = () => {
    const intl = useIntl();
    const actionRef = useRef<ActionType>();
    const formRef = useRef<ProFormInstance>();

    const [showSwapp, setShowSwapp] = useState<AppSwappState>({ show: false });

    const columns: ProColumns<AppManagerAPI.App>[] = [
        {
            title: "应用ID",
            dataIndex: 'appId'
        },
        {
            title: "分类",
            dataIndex: 'options',
            render: (_, record) => {
                return record.options?.category
            }
        },
        {
            title: "名称",
            dataIndex: 'options',
            render: (_, record) => {
                return record.options?.name
            }
        },
        {
            title: "ICON",
            dataIndex: 'options',
            render: (_, record) => (
                <img src={record.options?.logoImg} />
            ),
        },
        {
            title: "描述",
            dataIndex: 'options',
            render: (_, record) => {
                return record.options?.description
            }
        },
        {
            title: "来源",
            dataIndex: 'options',
            render: (_, record) => {
                return record.options?.source
            }
        },
        {
            title: "版本",
            dataIndex: 'options',
            render: (_, record) => {
                return record.options?.version
            }
        },
        {
            title: "定义",
            valueType: 'option',
            render: (_, record) => (
                <Space>
                    <Button onClick={() => {
                        setShowSwapp({ show: true, swapp: record.options?.swapp });
                    }}>
                        查看
                    </Button>
                </Space>
            ),
        },
        {
            title: "内置应用",
            dataIndex: 'options',
            valueType: 'switch',
            render: (_, record) => (
                <Switch checked={record.options?.builtIn} />
            )
        },
    ];

    return (
        <PageContainer>
            <ProTable<AppManagerAPI.App>
                search={false}
                rowKey="id"
                actionRef={actionRef}
                formRef={formRef}
                columns={columns}
                pagination={{ showQuickJumper: true, showSizeChanger: true, defaultPageSize: 10 }}
                request={(params, sorter, filter) => {
                    const queryParam: AppManagerAPI.AppParam = {
                        ...params,
                        page: params.current,
                        pageSize: params.pageSize,
                        pagination: true,
                    }
                    return AppService.list(queryParam);
                }}
                options={false}
            />

            {showSwapp.show && <Modal
                width={'50%'}
                open={showSwapp.show}
                destroyOnHidden={true}
                onOk={() => {
                    setShowSwapp({ show: false });
                }}
                onCancel={() => {
                    setShowSwapp({ show: false });
                }}
            >
                <pre>{showSwapp.swapp}</pre>
            </Modal>}
        </PageContainer>
    )
}

export default App