import { Injectable, BadRequestException } from '@nestjs/common';
import { S3Client, PutObjectCommand } from '@aws-sdk/client-s3';
import { getSignedUrl } from '@aws-sdk/s3-request-presigner';
import { v4 as uuidv4 } from 'uuid';

@Injectable()
export class S3Service {
  private s3Client: S3Client;

  constructor() {
    this.s3Client = new S3Client({
      region: process.env.AWS_REGION || 'ap-southeast-1',
    });
  }

  async getPresignedUrl(cognitoSub: string, contentType: string, fileName: string) {
    const bucket = process.env.S3_AVATAR_BUCKET || 'homever-avatars-local-dev';

    if (!contentType.startsWith('image/')) {
        throw new BadRequestException('Only images are allowed');
    }

    const uniqueId = uuidv4();
    const extension = fileName.split('.').pop() || 'jpg';
    const objectKey = `avatars/${cognitoSub}/${uniqueId}.${extension}`;

    const command = new PutObjectCommand({
      Bucket: bucket,
      Key: objectKey,
      ContentType: contentType,
    });

    // Valid for 5 minutes
    const uploadUrl = await getSignedUrl(this.s3Client, command, { expiresIn: 300 });

    return {
      uploadUrl,
      fileKey: objectKey,
      expiresIn: 300,
    };
  }
}
