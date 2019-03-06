# post-image-to-aliyun-oss

This example shows download files from post requests of url list and upload to Aliyun OSS buckets. 

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

You require data via only one url available. `http://localhost:9000/v1/image/upload` 

 `UploadController` method `process` take a post request of url list,
 generate a jobid and send a Response. 
 
 Before the procedure returns the Response, it sends an async proc for each url in the request list.
 `AuthClient.transfer`Download file from the url as an inputStream.
  It gives a filename of 40 alphanumeric plus `.jpg` as postfix.
 Then auth to oss and putObject to the oss bucket.
If any picture fails to be pushed, it gives a logger error to the screen on the backend.
 
 