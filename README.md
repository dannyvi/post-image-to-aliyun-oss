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

And open [http://localhost:9000/](http://localhost:9000/)


