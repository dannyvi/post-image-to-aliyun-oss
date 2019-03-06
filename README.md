# post-image-to-aliyun-oss



This example shows form processing and form helper handling under Play 2.6.x.

## How to run

You should config some environment variables for your aliyun OSS accounts. 

```bash
export OSS_ENDPOINT="<your oss endpoint>"
export OSS_ACCESSKEYID="<your oss access id>"
export OSS_ACCESSKEYSECRET="<your oss access password>"
export OSS_BUCKET="<bucket name>"
```

Put the contents above to `~/.profile`, or else where that the shell script will load on startup.

Start the Play app:

```bash
sbt run
```

Then the server works on [http://localhost:9000/](http://localhost:9000/), you can test with:

```bash
curl   --header "Content-type: application/json"   --request POST   --data
 '{"url": ["https://imgstorev.oss-cn-beijing.aliyuncs.com/00003001ac2eabe0db2039ed650048de1609b5de.jpg",
  "https://imgstorev.oss-cn-beijing.aliyuncs.com/0002a56498c539e7360526a615ffb3147603b7de.png"]}'   
  http://localhost:9000/v1/image/upload
```
