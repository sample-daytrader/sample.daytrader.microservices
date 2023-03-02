# Daytrader

Note: adapted from robot-shop
----

Use this helm chart to customise your install of daytrader.

### Helm v2.x

```shell
$ helm install --name daytrader --namespace daytrader .
```

### Helm v3.x

```bash
$ helm install daytrader --namespace daytrader --create-namespace.
```

## Images

By default the images are pulled from Docker Hub. Setting `image.repo` this can be changed, for example:

```shell
$ helm install --set image.repo=us.icr.io/daytrader/daytrader_beta/ ...
```

Will pull images from the us.icr.io.

By default the latest version of the images is pulled. A specific version can be used:

```shell
$ helm install --set image.version=0.1.2 ...
```

It is recommened to always use the latest version.

## Pod Security Policy

If you wish to enable [PSP](https://kubernetes.io/docs/concepts/policy/pod-security-policy/)

```shell
$ helm install --set psp.enabled=true ...
```




### MiniShift

Openshift is like K8s but not K8s. Set `openshift` to true or things will break. See the notes and scripts in the OpenShift directory of this repo.

```shell
$ helm install daytrader --set openshift=true helm
```

## Deployment Parameters

| Key              | Default | Type   | Description |
| ---------------- | ------- | ------ | ----------- |
| image.pullPolicy | IfNotPresent | string | Kubernetes pull policy. One of Always,IfNotPresent, or Never. |
| image.repo       | daytrader | string | Base docker repository to pull the images from. |
| image.version    | latest | string | Docker tag to pull. |
| nodeport         | false | booelan | Whether to expose the services via node port. |
| openshift        | false | boolean | If OpenShift additional configuration is applied. |
| psp.enabled      | false | boolean | Enable Pod Security Policy for clusters with a PSP Admission controller |
| ocCreateRoute    | false | boolean | If you are running on OpenShift and need a Route to the web service, set this to `true` |
| `<workload>`.affinity    | {} | object | Affinity for pod assignment on nodes with matching labels (Refer [here](https://kubernetes.io/docs/concepts/scheduling-eviction/assign-pod-node/#affinity-and-anti-affinity)) |
| `<workload>`.nodeSelector    | {} | object | Node labels for pod assignment (Refer [here](https://kubernetes.io/docs/concepts/scheduling-eviction/assign-pod-node/#nodeselector)) |
| `<workload>`.tolerations    | [] | list | Tolerations for pod assignment on nodes with matching taints (Refer [here](https://kubernetes.io/docs/concepts/scheduling-eviction/taint-and-toleration/)) |
---
> ### Notes for `affinity` and `tolerations`
> `<workload>` can be substituted with the different microservices consisting of Robot shop, namely:
> `affinity`, `nodeSelector` and `tolerations` can be set for individual workloads.
------
## Examples for deployment using `affinities` and `tolerations`
<br />

`values.yaml`
```yaml
.
..
...
accounts:
    gateway: null
    affinity:
        nodeAffinity:
        requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
            - matchExpressions:
                - key: node-restriction.kubernetes.io/pool_0
                    operator: Exists
                    values: []
    tolerations:
        - key: "pool_0"
        operator: "Equal"
        value: "true"
        effect: "NoSchedule"
        - key: "pool_0"
        operator: "Equal"
        value: "true"
        effect: "NoExecute"
    nodeSelector: {}

web:
    affinity:
        nodeAffinity:
        requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
            - matchExpressions:
                - key: node-restriction.kubernetes.io/pool_1
                    operator: Exists
                    values: []
    tolerations:
        - key: "pool_1"
        operator: "Equal"
        value: "true"
        effect: "NoSchedule"
        - key: "pool_1"
        operator: "Equal"
        value: "true"
        effect: "NoExecute"
    nodeSelector: {}
...
..
.
 ```

In this example, the `accounts` Pods will be deployed on only those nodes that have the label `node-restriction.kubernetes.io/pool_0` and are tainted using
```
kubectl taint node <node_name> pool_0=true:NoSchedule
kubectl taint node <node_name> pool_0=true:NoExecute
```

Similarly, the `web` Pods will be deployed on only those nodes that have the label `node-restriction.kubernetes.io/pool_1` and are tainted using
```
kubectl taint node <node_name> pool_1=true:NoSchedule
kubectl taint node <node_name> pool_1=true:NoExecute
```

Hence, this way we can control which `daytrader` workloads are running on which nodes/nodepools.

> *Note*: `nodeSelector` will behave in a similar fashion.
