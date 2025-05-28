import { useState, type FC } from 'react';
import { PageContainer } from '@ant-design/pro-components';
import LocalMarket from './LocalMarket';

const AppManagerMarketWeb: FC = () => {

  const [tabKey, setTabKey] = useState<string>('local');

  const tabList = [
    {
      key: 'local',
      tab: '本地仓库',
    },
    {
      key: 'cloud',
      tab: '云端仓库',
    }
  ];

  const renderChild = () => {
    if (tabKey === 'component') {
      return <LocalMarket />
    } else if (tabKey === 'cloud') {
      return <LocalMarket />
    }
    return <LocalMarket />
  }

  return (
    <PageContainer
      tabList={tabList}
      onTabChange={setTabKey}
    >
      {renderChild()}
    </PageContainer>
  )
}

export default AppManagerMarketWeb;