import { useState, type FC } from 'react';
import { PageContainer } from '@ant-design/pro-components';
import { Outlet } from '@umijs/max';
import ComponentPlugin from './ComponentPlugin';
import TraitPlugin from './TraitPlugin';

const AppManagerPluginWeb: FC = () => {

  const [tabKey, setTabKey] = useState<string>('component');

  const tabList = [
    {
      key: 'component',
      tab: '组件(Component)',
    },
    {
      key: 'trait',
      tab: '运维特征(Trait)',
    }
  ];

  const renderChild = () => {
    if (tabKey === 'component') {
      return <ComponentPlugin />
    } else if (tabKey === 'trait') {
      return <TraitPlugin />
    }
    return <ComponentPlugin />
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

export default AppManagerPluginWeb;