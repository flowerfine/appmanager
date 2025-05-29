import sys
import yaml
import os

h = open(sys.argv[1] + "/meta.yaml", 'r')
content = yaml.safe_load(h.read())
h.close()

registry = os.environ.get("IMAGE_REGISTRY")

for package in content.get("componentPackages",[]):
    packageExt = yaml.safe_load(package.get("packageExt"))
    containers = packageExt.get("spec",{}).get("workload",{}).get("spec", {}).get("containers",[])
    initContainers = packageExt.get("spec",{}).get("workload",{}).get("spec", {}).get("initContainers",[])
    for c in containers + initContainers:
        package["packageExt"] = package["packageExt"].replace(c["image"], registry + "/" + c["image"].split("/")[-1])

h = open(sys.argv[1] + "/meta.yaml", 'w')
h.write(yaml.safe_dump(content, width=float("inf")))
h.close()