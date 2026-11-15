import {
  Injectable,
  InternalServerErrorException,
  BadRequestException,
} from '@nestjs/common';
import { createClient, SupabaseClient } from '@supabase/supabase-js';
import { randomUUID } from 'crypto';

@Injectable()
export class StorageService {
  private supabase: SupabaseClient;

  constructor() {
    const supabaseUrl = process.env.SUPABASE_URL;
    const serviceRoleKey = process.env.SUPABASE_SERVICE_ROLE_KEY;

    if (!supabaseUrl || !serviceRoleKey) {
      throw new Error(
        'Supabase configuration missing (SUPABASE_URL or SUPABASE_SERVICE_ROLE_KEY)',
      );
    }

    this.supabase = createClient(supabaseUrl, serviceRoleKey);
  }

  async uploadImage(file: Express.Multer.File, userId: string) {
    const allowedTypes = ['image/jpeg', 'image/png', 'image/webp'];

    if (!file || !file.mimetype || !allowedTypes.includes(file.mimetype)) {
      throw new BadRequestException(
        'Unsupported image type. Allowed: JPEG, PNG, WEBP',
      );
    }

    const extensionMap: Record<string, string> = {
      'image/jpeg': 'jpg',
      'image/png': 'png',
      'image/webp': 'webp',
    };
    const extension = extensionMap[file.mimetype] || 'jpg';
    const filePath = `users/${userId}/${randomUUID()}.${extension}`;
    const bucket = process.env.SUPABASE_BUCKET || 'homever-images';

    const { error } = await this.supabase.storage
      .from(bucket)
      .upload(filePath, file.buffer, {
        contentType: file.mimetype,
        upsert: false,
      });

    if (error) {
      throw new InternalServerErrorException(
        `Supabase upload error: ${error.message}`,
      );
    }

    const { data } = this.supabase.storage
      .from(bucket)
      .getPublicUrl(filePath);

    return {
      path: filePath,
      url: data.publicUrl,
    };
  }
}

