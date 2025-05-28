import React from "react";
import {getIntl, getLocale} from "@umijs/max";
import {Graph, Node, register, XFlow} from "@antv/xflow";
import moment from "moment";
import useStyles from './style';

const CICD_NODE = 'cicd-node';
const CICD_EDGE = 'cicd-edge';

const statusImage = {
  success: 'https://mdn.alipayobjects.com/huamei_d2ejos/afts/img/A*sko9RoPu-HgAAAAAAAAAAAAADvl6AQ/original',
  failure: 'https://gw.alipayobjects.com/mdn/rms_43231b/afts/img/A*SEISQ6My-HoAAAAAAAAAAAAAARQnAQ',
  running: 'https://mdn.alipayobjects.com/huamei_d2ejos/afts/img/A*7gzVQJ63mfEAAAAAAAAAAAAADvl6AQ/original',
  pending: 'https://mdn.alipayobjects.com/huamei_d2ejos/afts/img/A*Em_PQoTrMDgAAAAAAAAAAAAADvl6AQ/original'
};

const CICDNode = ({node}: { node: Node }) => {
  const intl = getIntl(getLocale())
  const {styles} = useStyles();
  const data = node?.getData()
  const {label, meta, attrs, extData} = data;
  const {startTime, endTime, status, outputs, tasks = []} = extData;

  return (
    <XFlow>
      <div className={styles.cicdNodeWrap}>
        <div className={styles.stepLabel}>{label}</div>
        <div className={styles.cicdNode}>
          <div className={styles.stepBox}>
            <div className={styles.icon}>
              <img src={meta.icon} alt={meta.type}/>
            </div>
            <div className={styles.wrap}>
              <div className={styles.title}>{meta.label}</div>
              <div className={styles.desc}>{moment(endTime).diff(moment(startTime), 'seconds') + '秒'}</div>
            </div>
          </div>

          {tasks.length > 0 && (
            <div className={styles.tasks}>
              {tasks.map((item, index) => (
                <div style={{position: 'relative'}} key={item.name + index}>
                  <div className={styles.taskBox}>
                    <div className={styles.icon}>
                      {item.status === 'success' && <img src={statusImage.success} alt="success"/>}
                    </div>
                    <div className={styles.wrap}>
                      <div className={styles.title}>{item.name}</div>
                      <div
                        className={styles.desc}>{moment(item.endTime).diff(moment(item.startTime), 'seconds') + '秒'}</div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </XFlow>
  );
}

register({
  shape: CICD_NODE,
  width: 260,
  height: 100,
  component: CICDNode,
  // port默认不可见
  ports: {
    groups: {
      in: {
        position: {
          name: 'left',
          args: {
            dx: 4,
          },
        },
        attrs: {
          circle: {
            r: 4,
            magnet: true,
            stroke: 'transparent',
            strokeWidth: 1,
            fill: 'transparent',
          },
        },
      },

      out: {
        position: {
          name: 'right',
          args: {
            dx: -4,
          },
        },

        attrs: {
          circle: {
            r: 4,
            magnet: true,
            stroke: 'transparent',
            strokeWidth: 1,
            fill: 'transparent',
          },
        },
      },
    },
  },
})

Graph.registerEdge(
  CICD_EDGE,
  {
    markup: [
      {
        tagName: 'path',
        selector: 'wrap',
        attrs: {
          fill: 'none',
          cursor: 'pointer',
          stroke: 'transparent',
          strokeLinecap: 'round',
        },
      },
      {
        tagName: 'path',
        selector: 'line',
        attrs: {
          fill: 'none',
          pointerEvents: 'none',
        },
      },
    ],
    connector: 'smooth',
    attrs: {
      wrap: {
        connection: true,
        strokeWidth: 10,
        strokeLinejoin: 'round',
      },
      line: {
        connection: true,
        stroke: '#A2B1C3',
        strokeWidth: 1,
        targetMarker: {
          name: 'classic',
          size: 6,
        },
      },
    },
  },
  true)

export {CICD_NODE, CICD_EDGE};
