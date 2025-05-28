import { AppManagerAPI } from "@/services/appmanager/typings";
import { ProList } from "@ant-design/pro-components";
import { FC } from "react";


const LocalMarketApp: FC = () => {

    return (
        <ProList<AppManagerAPI.Plugin>
        itemCardProps={{
            ghost: true,
          }}
          pagination={{
            defaultPageSize: 8,
            showSizeChanger: false,
          }}
        />
    )
}

export default LocalMarketApp;