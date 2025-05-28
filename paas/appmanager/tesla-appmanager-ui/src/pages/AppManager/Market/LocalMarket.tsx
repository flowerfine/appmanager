import { PageContainer } from "@ant-design/pro-components";
import { Tabs } from "antd";
import { Children, FC, useState } from "react";
import LocalMarketApp from "./LocalMarketApp";

const LocalMarket: FC = () => {

    const [tabKey, setTabKey] = useState<string>('local');

    const tabList = [
        {
            key: 'apps',
            label: '本地应用',
            children: <LocalMarketApp />
        },
        {
            key: 'plugins',
            label: '本地插件',
            children: <div>本地插件</div>
        }
    ];

    return (
        <PageContainer
            breadcrumbRender={false}
            title={false}
            content={false}
        >
            <Tabs
                defaultActiveKey={tabKey}
                items={tabList}
                onChange={setTabKey}
                tabPosition={'left'}
            />
        </PageContainer>
    )
}

export default LocalMarket;