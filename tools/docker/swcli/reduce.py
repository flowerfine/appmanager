    import sys
    import yaml
    import json
    from functools import reduce

    def merge(a, b):
        if b is None:
            return a
        if a is None:
            return b
        parameterValues = [x["name"] for x in a.get("spec").get("parameterValues")]
        for p in b.get("spec",{}).get("parameterValues",[]):
            if p["name"] in parameterValues: continue
            if p["name"] == "COMPONENT_NAME": continue
            a["spec"]["parameterValues"].append(p)
        a["spec"]["components"] += b["spec"]["components"]
        return a

    res = reduce(merge, [yaml.safe_load(raw) for raw in sys.stdin.read().strip().split("---")])
    print(yaml.dump(res))